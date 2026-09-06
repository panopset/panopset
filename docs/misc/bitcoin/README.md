Just trying to understand bitcoin knots...

    sudo apt install libboost-all-dev libsqlite3-dev sqlite3
    cd w
    git clone github:/bitcoinknots/bitcoin
    cd bitcoin
    cmake -B build -D RDTS_CONSENT=IMPLICIT
    cd build
    sudo make
