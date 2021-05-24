#!/usr/bin/env bash

distProject="../AsmHelperPlugin"

cpCode(){
  echo "copy new code to project"
  cp -rf AsmPlugin/* distProject
}

goDistProject() {
cd $distProject
}

pushCode() {
  echo "add new code"
  git add .
  git commit -m "add some code"
  git push origin master
  git diff
}

listTag(){
  git tag
}

createTag(){
  git tag $1
}

  if [ $# == 0 ]; then
    echo "sh helper.sh sync|push|tag v1.0.1"
  elif [ $1 == "sync" ]; then
    echo "sync string from a to b"
    cpCode
  elif [ $1 == "push" ]; then
    echo "push dest project to b"
    goDistProject
    pushCode
    listTag
  elif [ $1 == "tag" ]; then
    echo "create tag from a"
    createTag $2
#    syncDevStr2Test
  fi