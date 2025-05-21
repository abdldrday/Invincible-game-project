
package inventory;

public class Item implements InventoryComponent {
    private String name;

    public Item(String name) {
        this.name = name;
    }

    @Override
    public void add(InventoryComponent component) {
        throw new UnsupportedOperationException("Leaf cannot add components");
    }

    @Override
    public void remove(InventoryComponent component) {
        throw new UnsupportedOperationException("Leaf cannot remove components");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public String toString() {
        return name;
    }
}
