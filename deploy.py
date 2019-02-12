#!/usr/bin/python
import sys
import os

if len(sys.argv) == 1:
    quit("Usage: %s [Version]" % os.path.dirname(__file__))

version= sys.argv[1]

os.chdir(os.path.dirname(__file__))

def addVersion(path, key, data):
    filedata = []

    with open(path, 'rb') as f:
        for l in f.readlines():
            if key in l:
                l = data % version
            filedata.append(l)
           

    with open(path, "wb") as f:
        f.writelines(filedata)

addVersion('build.gradle', "//$VERSION$", 'version = "%s" //$VERSION$\n')
addVersion('src/plugin.yml', "#$version$", 'version: %s #$version$\n')

quit()
os.system('git tag %s'% version)
os.system('git add -A')
os.system('git commit')
os.system('git push origin master --tags')
