# !/bin/bash

echo "staging $1 to $2."
mv -f /var/www/$2/html/index.html .
rm -rf /var/www/$2/html
rsync -avuzh /var/www/$1/html /var/www/$2/
mv -f ./index.html /var/www/$2/html/index.html

