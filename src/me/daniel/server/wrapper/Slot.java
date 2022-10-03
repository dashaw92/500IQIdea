package me.daniel.server.wrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//An item with optional associated NBT (enchantments)
public final class Slot {

    private static final List<Integer> ENCHANTABLE = List.of(
            //TOOLS
            0x103, //Flint and steel
            0x105, //Bow
            0x15A, //Fishing rod
            0x167, //Shears
            //sword, shovel, pickaxe, axe, hoe
            0x10C, 0x10D, 0x10E, 0x10F, 0x122, //WOOD
            0x110, 0x111, 0x112, 0x113, 0x123, //STONE
            0x10B, 0x100, 0x101, 0x102, 0x124, //IRON
            0x114, 0x115, 0x116, 0x117, 0x125, //DIAMOND
            0x11B, 0x11C, 0x11D, 0x11E, 0x126, //GOLD
            //ARMOUR
            //helmet, chestplate, leggings, boots
            0x12A, 0x12B, 0x12C, 0x12D, //LEATHER
            0x12E, 0x12F, 0x130, 0x131, //CHAIN
            0x132, 0x133, 0x134, 0x135, //IRON
            0x136, 0x137, 0x138, 0x139, //DIAMOND
            0x13A, 0x13B, 0x13C, 0x13D  //GOLD
    );

    public final short id, count, damage;
    private final Map<Short, Short> ench = new HashMap<>();

    public Slot(short id, short count, short damage) {
        this.id = id;
        this.count = count;
        this.damage = damage;
    }

    public boolean canAcceptEnchants() {
        return ENCHANTABLE.contains((int)id);
    }

    public void addEnchant(short id, short lvl) {
        ench.put(id, lvl);
    }

    public Map<Short, Short> getEnch() {
        return Collections.unmodifiableMap(ench);
    }
}
