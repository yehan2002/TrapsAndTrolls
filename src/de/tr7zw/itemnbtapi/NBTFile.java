package de.tr7zw.itemnbtapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

public class NBTFile extends NBTCompound {

    private final File file;
    private Object nbt;

    public NBTFile(File file) throws IOException {
        super(null, null);
        this.file = file;
        if (file.exists()) {
            FileInputStream inputsteam = new FileInputStream(file);
            nbt = NBTReflectionUtil.readNBTFile(inputsteam);
        } else {
            nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
            save();
        }
    }

    public void save() throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream outStream = new FileOutputStream(file);
        NBTReflectionUtil.saveNBTFile(nbt, outStream);
    }

    public File getFile() {
        return file;
    }

    protected Object getCompound() {
        return nbt;
    }

    protected void setCompound(Object compound) {
        nbt = compound;
    }

}
