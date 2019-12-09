import sys
import builder
import time
import pickle
import restrictive_pickle
from os import path
import generator
import random


def randomizer(bound):  # Define a randomizer function to pass to the generator
    return random.randint(1, bound)


# Markov Chain Generation program
# Example: python markov.py books/aladdin.txt 25
# *Larger text sources may take longer to build
if __name__ == '__main__':
    args = sys.argv
    usage = 'Usage: %s ([input-file-name] <num-words>)' % (args[0],)  # Usage string

    if len(args) != 3 and len(args) != 2:
        raise ValueError(usage)  # If the number of arguments doesn't match, raise an error

    if len(args) == 3:  # If there is a file name and word count
        num_words = int(args[2])  # Set word count
        if not path.exists("chain.p"):  # Check for existing chain
            start = time.clock()
            chain = builder.build(args[1])  # If no existing chain, build a new one
        else:
            chain = restrictive_pickle.restricted_loads(open("chain.p", 'rb').read())
            start = time.clock()
            chain = builder.build(args[1], chain)  # If chain exists, load it and build on top of it
        pickle.dump(chain, open("chain.p", 'wb'))  # Dump the chain to a file
        print("Time to run: {}".format(time.clock() - start))

    elif len(args) == 2:  # If there is just word count
        num_words = int(args[1])  # Set word count
        chain = restrictive_pickle.restricted_loads(open("chain.p", 'rb').read())  # Use restricted loading

    outstr = generator.generate(chain, randomizer, num_words, builder.NONWORD)  # Generate a sentence
    print(outstr)

