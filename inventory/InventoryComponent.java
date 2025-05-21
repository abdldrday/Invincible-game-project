
package inventory;

public interface InventoryComponent {
    void add(InventoryComponent component);
    void remove(InventoryComponent component);
    String getName();
    int getCount();
}
