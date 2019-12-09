import prefix
import suffix
from functools import reduce


NONWORD = "\n"  # End of line character


# Chains are ImmDicts with keys consisting of prefix 'states': a two-word tuple representing the current state
# and an ImmDict representing weighted suffixes (how often each suffix appears) as a value
def build(input_file, chain=None):
    if chain is not None:  # If there is an existing chain, build on top of it
        return build_chain(add_to_chain, pairs_gen(input_file, line_gen), chain)
    else:  # If there is no existing chain, begin from an empty ImmDict
        return build_chain(add_to_chain, pairs_gen(input_file, line_gen), suffix.empty_suffix())


def pairs_gen(input_file, linegen):  # Generate pairs of prefixes and suffixes from lines of text
    current_pair = prefix.new_prefix(NONWORD, NONWORD)  # Start with an empty prefix
    for line in linegen(input_file):
        words_list = line.split(" ")  # Split line of text into words separated by spaces
        while len(words_list) > 0:
            new_word = words_list.pop(0)  # As long as there are words left, pop one from the list
            yield (current_pair, new_word)  # Yield the current state and the next word
            current_pair = prefix.shift_in(current_pair, new_word)  # Shift the new word into the current pair
    yield (current_pair, NONWORD)  # The final pair ends with a NONWORD


def line_gen(input_file):  # Iteratively return lines of text from input
    for line in open(input_file):
        yield line.strip()  # Strip leading/trailing whitespace


def add_to_chain(chain, pair):
    if chain.get(pair[0]):
        # Place the current state plus the next word into the chain
        # pair[0] is the two-word prefix tuple representing the current state
        # suffix.add_word returns a new suffix ImmDict with the updated value
        return chain.put(pair[0], suffix.add_word(chain.get(pair[0]), pair[1]))
    else:
        # If the current state is new, initialize the suffix ImmDict
        return chain.put(pair[0], suffix.add_word(suffix.empty_suffix(), pair[1]))


def build_chain(add_func, pairs_func, immdict_initializer):
    return reduce(add_func, pairs_func, immdict_initializer)  # Add pairs to the chain, starting with empty ImmDict
