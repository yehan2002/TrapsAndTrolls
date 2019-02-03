#!/usr/bin/python
import sys
import os

if len(sys.argv) == 1:
    quit("Usage: %s [Version]" % os.path.dirname(__file__))

v = sys.argv[1]

os.chdir(os.path.dirname(__file__))

filedata = []

with open('build.gradle', 'rb') as f:
    for l in f.readlines():
        if "//$VERSION$" in l:
            l = 'version = %s //$VERSION$\n'
        filedata.append(filedata)
            
with open("build.gradle", "wb") as f:
    f.writelines(l)

os.system('git tag %s'%v)
os.system('git add -A')
os.system('git commit')
os.system('git push origin master --tags')
