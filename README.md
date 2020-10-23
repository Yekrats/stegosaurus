# Stegosaurus

Private code for Scott Starkey's "Stegosaurus" project.
See a demo of the original trick at https://www.penguinmagic.com/p/6252
The code is used for my Animal Themed deck at https://www.thegamecrafter.com/games/stegosaurus-animal-themed-poker-size

## Important sections and info

The algorithm is a binary code of letters. In the base effect, cards either have letters for the left side of the keyboard or the right side.
The list of 32 5-letter words is numbered 0 to 31. The first letter position is worth 16 points, the second letter is worth 8 points, and so on, so
that each word has a unique position in the list in binary numbers.

The `score-word` function accepts a word and an optional regex. (If no regex is supplied, it will use the default regex as in the original effect.)
It will provide the binary score of the word given.

The `all-english-words-sorted` function (no parameters) will show each of the scores for all 5-letter words in the dictionary. 
Call like `(all-english-words-sorted)`.

The def `alpha-combo2` is what I used to pare the 2 million combinations of regexes down to 6 possibilities to fill every slot of my 
animals.txt word list.  ***Warning!*** This takes hours to realize the full lazy sequence on my old MacBook Pro. If I made this again, I would probably
lean on multi-threading to reduce the time. However, something like `(first alfpha-combo2)` is safe and much faster. Once I determined the 6 usable
regexes, I plugged them into a simple loop to give me the proper animal list to use. (See lines 179-190.)

## License

Copyright © 2020

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

