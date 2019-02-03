package de.tr7zw.itemnbtapi;

import java.util.HashSet;
import java.util.Set;

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

public class NBTListCompound {

    private NBTList owner;
    private Object compound;

    protected NBTListCompound(NBTList parent, Object obj) {
        owner = parent;
        compound = obj;
    }

    public void setString(String key, String value) {
        if (value == null) {
            remove(key);
            return;
        }
        try {
            compound.getClass().getMethod("setString", String.class, String.class).invoke(compound, key, value);
            owner.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setInteger(String key, int value) {
        try {
            compound.getClass().getMethod("setInt", String.class, int.class).invoke(compound, key, value);
            owner.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getInteger(String value) {
        try {
            return (int) compound.getClass().getMethod("getInt", String.class).invoke(compound, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setDouble(String key, double value) {
        try {
            compound.getClass().getMethod("setDouble", String.class, double.class).invoke(compound, key, value);
            owner.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public double getDouble(String key) {
        try {
            return (double) compound.getClass().getMethod("getDouble", String.class).invoke(compound, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }


    public String getString(String key) {
        try {
            return (String) compound.getClass().getMethod("getString", String.class).invoke(compound, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public boolean hasKey(String key) {
        try {
            return (boolean) compound.getClass().getMethod("hasKey", String.class).invoke(compound, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public Set<String> getKeys() {
        try {
            return (Set<String>) ReflectionMethod.LISTCOMPOUND_GET_KEYS.run(compound);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new HashSet<>();
    }

    public void remove(String key) {
        try {
            compound.getClass().getMethod("remove", String.class).invoke(compound, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
