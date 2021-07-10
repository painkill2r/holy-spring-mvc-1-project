package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    /* @RequiredArgsConstructor 설정으로 인한 주석 처리
    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    */

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    /**
     * 상품 목록 페이지
     *
     * @param model
     * @return
     */
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }

    /**
     * 상품 정보 페이지
     *
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * 상품 등록 페이지
     *
     * @param item
     * @return
     */
    @GetMapping("/add")
    public String addForm(@ModelAttribute("item") Item item) {
        return "basic/addForm";
    }

    /**
     * 상품 등록 처리
     *
     * @param item
     * @param redirectAttributes RedirectAttributes를 사용하면 URL 인코딩도 해주고, PathVariable, Query Parameter까지 처리해 줌.
     * @return
     */
    @PostMapping("/add")
    public String save(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);

        // model.addAttribute("item", item); // @ModelAttibute를 통해 자동으로 Model에 추가됨으로 생략 가능.

        redirectAttributes.addAttribute("itemId", savedItem.getId()); // PathVariable 바인딩(/basic/items/{itemId} -> /basic/items/3)
        redirectAttributes.addAttribute("status", true); // Query Parameter로 처리(?status=true)

        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 상품 수정 페이지
     *
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        model.addAttribute("item", item);

        return "basic/editForm";
    }

    /**
     * 상품 수정 처리
     *
     * @param itemId
     * @param updateParam
     * @return
     */
    @PostMapping("/{itemId}/edit")
    public String update(@PathVariable Long itemId, @ModelAttribute("item") Item updateParam) {
        itemRepository.update(itemId, updateParam);

        return "redirect:/basic/items/{itemId}"; // {itemId}는 @PathVariable Long itemId의 값을 그대로 사용함.
    }
}
