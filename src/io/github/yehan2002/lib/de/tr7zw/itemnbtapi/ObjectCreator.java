package io.github.yehan2002.lib.de.tr7zw.itemnbtapi;

import java.lang.reflect.Constructor;

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

public enum ObjectCreator {
    NMS_NBTTAGCOMPOUND(ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()),
    NMS_BLOCKPOSITION(ClassWrapper.NMS_BLOCKPOSITION.getClazz(), int.class, int.class, int.class);

    private Constructor<?> construct;

    ObjectCreator(Class<?> clazz, Class<?>... args){
        try{
            construct = clazz.getConstructor(args);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public Object getInstance(Object... args){
        try{
            return construct.newInstance(args);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
}
