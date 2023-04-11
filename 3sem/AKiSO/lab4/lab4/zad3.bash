#!/bin/bash

#cat image
cat_url=$(curl -s @{'api_key=61cf8b2f-5c2f-48a4-a37e-28ab225f7855'} https://api.thecatapi.com/v1/images/search | jq '.[] |.url')
cat_url=$(sed -e 's/^"//' -e 's/"$//' <<<"$cat_url")
echo $cat_url
curl -s -X GET $cat_url > cat.jpg

#display image
catimg cat.jpg

#new line
echo " "

#random chuck norris joke
curl -s http://api.icndb.com/jokes/random | jq '.value | .joke'

#new line
echo " "