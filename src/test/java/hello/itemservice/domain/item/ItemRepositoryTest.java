package hello.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Item item = new Item("itemA", 10000, 10);

        //when
        Item saveItem = itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(item.getId());

        // 등록된 상품이 등록하기 위해 요청 데이터로 보낸 상품 정보와 같은지 확인
        assertThat(findItem).isEqualTo(saveItem);
    }

    @Test
    void findAll() {
        //given
        Item item1 = new Item("itemA", 10000, 10);
        Item item2 = new Item("itemB", 20000, 15);

        itemRepository.save(item1);
        itemRepository.save(item2);

        //when
        List<Item> result = itemRepository.findAll();

        //then
        // 상품 저장소 크기 및 저장했던 상품 정보를 가지고 있는지 확인
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(item1, item2);
    }

    @Test
    void updateItem() {
        //given
        Item item = new Item("itemA", 10000, 10);

        Item savedItem = itemRepository.save(item);
        Long itemId = savedItem.getId();

        //when
        Item updateParam = new Item("itemC", 30000, 20);

        itemRepository.update(itemId, updateParam);

        //then
        Item findItem = itemRepository.findById(itemId);

        // 수정된 상품 정보와 수정하려고 요청 데이터로 보낸 상품 정보가 동일한지 확인
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }
}