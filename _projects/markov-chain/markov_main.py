import builder
import generator
import random
import restrictive_pickle
import time
import pickle
from os import path


def randomizer(bound):
    return random.randint(1, bound)  # Define randomizer function


# Test module for running markov chain generation
if __name__ == "__main__":
    file_name = 'books/aladdin.txt'  # Aladdin is the shortest text document
    if not path.exists("chain.p"):  # Check for existing chain
        start = time.clock()
        chain = builder.build(file_name)  # If there is no saved chain, generate one and time it
        pickle.dump(chain, open("chain.p", 'wb'))  # Dump the chain to a file
        print("Time to run: {}".format(time.clock() - start))
    else:
        chain = restrictive_pickle.restricted_loads(open("chain.p", 'rb').read())  # Use restricted file loading
    num_words = 25
    outstr = generator.generate(chain, randomizer, num_words, builder.NONWORD)  # Generate a sentence
    print(outstr)
