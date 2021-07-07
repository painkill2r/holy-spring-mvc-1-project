package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); // static, 실제로는 HashMap을 사용하면 안됨(멀티 쓰레드 환경에서 위험 요소 발생 가능성 있음. / ConcurrentHashMap 사용하는 걸 추천)
    private static long sequence = 0L; // static (AtomicLong 사용하는 것을 추천)

    /**
     * 등록
     *
     * @param item
     * @return
     */
    public Item save(Item item) {
        item.setId(++sequence);

        store.put(item.getId(), item);

        return item;
    }

    /**
     * 한 건 조회
     *
     * @param id
     * @return
     */
    public Item findById(Long id) {
        return store.get(id);
    }

    /**
     * 전체 조회
     *
     * @return
     */
    public List<Item> findAll() {
        // store.values() 자체를 반환해도 되는데,
        // ArrayList로 감싼 이유는 이후 List안에 각 Item이 변경되어도 store에는 영향을 주지 않기 위해서임.
        return new ArrayList<>(store.values());
    }

    /**
     * 수정
     *
     * @param itemId
     * @param updateParam ItemParamDTO 클래스를 만들어 사용하는 것을 추천함
     */
    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    /**
     * 저장소 비우기(테스트용)
     */
    public void clearStore() {
        store.clear();
    }
}
