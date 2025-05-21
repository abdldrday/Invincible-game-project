
package inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory implements InventoryComponent {
    private List<InventoryComponent> components = new ArrayList<>();
    private String name;

    public Inventory(String name) {
        this.name = name;
    }

    @Override
    public void add(InventoryComponent component) {
        components.add(component);
    }

    @Override
    public void remove(InventoryComponent component) {
        components.remove(component);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCount() {
        return components.stream().mapToInt(InventoryComponent::getCount).sum();
    }

    public List<InventoryComponent> getComponents() {
        return new ArrayList<>(components);
    }

    @Override
    public String toString() {
        return "Inventory{name='" + name + "', items=" + components + "}";
    }
}
