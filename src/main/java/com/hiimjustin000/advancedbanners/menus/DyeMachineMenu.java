package com.hiimjustin000.advancedbanners.menus;

import com.hiimjustin000.advancedbanners.items.AdvancedDyeItem;
import com.hiimjustin000.advancedbanners.registries.MenuTypeRegistry;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DyeMachineMenu extends AbstractContainerMenu
{
    private final SimpleContainer container = new SimpleContainer(1);
    private final ContainerLevelAccess access;

    public DyeMachineMenu(int id, Inventory inventory)
    {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public DyeMachineMenu(int id, Inventory inventory, ContainerLevelAccess levelAccess)
    {
        super(MenuTypeRegistry.DYE_MACHINE_MENU_TYPE.get(), id);
        access = levelAccess;

        addSlot(new Slot(container, 0, 27, 27)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return stack.getItem() instanceof AdvancedDyeItem;
            }
        });

        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                addSlot(new Slot(inventory, c + r * 9 + 9, 8 + c * 18, 67 + r * 18));
            }
        }

        for (int c = 0; c < 9; c++)
        {
            addSlot(new Slot(inventory, c, 8 + c * 18, 125));
        }
    }

    @Override
    public void removed(Player player)
    {
        super.removed(player);
        access.execute((level, pos) -> clearContainer(player, container));
    }

    public void changeColor(int color)
    {
        ItemStack stack = container.getItem(0).copy();
        stack.getOrCreateTag().putInt("Color", color);
        container.setItem(0, stack);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem())
        {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();
            if (index < 1)
            {
                if (!moveItemStackTo(stack1, 1, 37, true))
                    return ItemStack.EMPTY;
            }
            else if (!moveItemStackTo(stack1, 0, 1, false))
                return ItemStack.EMPTY;

            if (stack1.isEmpty())
                slot.setByPlayer(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (stack1.getCount() == stack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, stack1);
        }

        return stack;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return container.stillValid(player);
    }

    public SimpleContainer getContainer()
    {
        return container;
    }
}
