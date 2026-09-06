[home](../../../README.md)

# Panopset Tutorial: fas21.com

Panopset Blackjack was split out from Panopset, because
it is unrelated to the productivity oriented 
applications associated with Panopset, such as flywheel
and skyscraper. 

Also, there is no societal value to gambling. I like to play
blackjack once in a while, and it never ceases to amaze me
how people throw away their money on poor play. If you are going
to sit down at a blackjack table, at least memorize the basic
strategy. Fas21 (formerly Panopset Blackjack) helps you learn
the basic strategy, as well as practice counting cards using
a variety of strategies, which can be simulated at tens of thousands
of hands per second (t key).

I'm a compulsive degenerate gambler. I wrote fas21 because
none of the other blackjack apps had a fast deal feature
(irrelevant to this tutorial, but go to 
[fas21.com](https://fas21.com) for a full
explanation).

While splitting out fas21 from Panopset, I thought I'd write a
tutorial for publishing a Panopset multi-platform application, 
using custom branding.

Panopset is completely open source, so anyone may use it as a
springboard for quickly publishing their own ideas, without
worrying about re-inventing some of the basics, like standard
JavaFX menus to persist their desktop application documents.

For this tutorial, we build fas21.com from scratch! It's
just getting started, as you can see.

It is very tough these days to find an un-used good domain name.

For example, fast21 dot com was taken. Fortunately twentyone starts with a t,
so by dropping the t on the end of fast and re-using the implied first 
"t" in 21, I still managed to get a short and memorable domain name, 
which can quickly be associated with the added value of the product.

We'll register it with [Squarespace](https://squarespace.com), and host
it with [Digitalocean](https://digitalocean.com).  (Disclaimer: I am a
DOCN stockholder, you may use your own vendors.)

* First, you'll need a [server](server.md).
* Next, you'll need a [domain](domain.md).
* Next, you'll set up your server [block](block.md).

At this point we have a letsencrypt secured server block attached to fas21.com.

Next we'll need some content. For this, 

* Install [flutter](../../flutter/README.md)

Then


    cd docs/tutorials/fas21/flutter
    flutter create blackjack


Follow any instructions given from executing the flutter create, regarding
gradle/java compatibility.


    cd blackjack
    flutter analyze


Make sure you see "No issues found!", before continuing.


    flutter build web --base-href web
    cd ../../scripts
    ./deployFlutter.sh

