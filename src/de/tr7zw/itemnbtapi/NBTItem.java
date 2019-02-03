package de.tr7zw.itemnbtapi;

import org.bukkit.inventory.ItemStack;

/*
The MIT License (MIT)

Copyright (c) 2015 tr7zw

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/

public class NBTItem extends NBTCompound {

    private ItemStack bukkitItem;

    public NBTItem(ItemStack item) {
        super(null, null);
        if(item == null){
            throw new NullPointerException("ItemStack can't be null!");
        }
        bukkitItem = item.clone();
    }

    protected Object getCompound() {
        return NBTReflectionUtil.getItemRootNBTTagCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, bukkitItem));
    }

    protected void setCompound(Object compound) {
        Object stack = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, bukkitItem);
        ReflectionMethod.ITEMSTACK_SET_TAG.run(stack, compound);
        bukkitItem = (ItemStack) ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, stack);
    }

    public ItemStack getItem() {
        return bukkitItem;
    }

    protected void setItem(ItemStack item) {
        bukkitItem = item;
    }
    
    
    /**
     * This may return true even when the NBT is empty.
     * 
     * @return Does the ItemStack have a NBTCompound.
     */
    public boolean hasNBTData(){
        return getCompound() != null;
    }
    
    public static NBTContainer convertItemtoNBT(ItemStack item){
        return NBTReflectionUtil.convertNMSItemtoNBTCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, item));
    }
    
    public static ItemStack convertNBTtoItem(NBTCompound comp){
        return (ItemStack) ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, NBTReflectionUtil.convertNBTCompoundtoNMSItem(comp));
    }

}
