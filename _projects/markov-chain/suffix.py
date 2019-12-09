from immdict import ImmDict
from functools import reduce


def empty_suffix():
    return ImmDict()  # Default constructor for suffix


def add_word(chain, suffix):
    i = 1 if chain.get(suffix) is None else chain.get(suffix)+1  # Increment the number of times a suffix appears
    return chain.put(suffix, i)  # Return the new ImmDict from put()


def recursive_choice(counter, suffix_dict, suffix_keys):  # Choose a random suffix based on a randomized initial counter
    counter -= suffix_dict.get(suffix_keys[0])  # Subtract the number of times the first suffix appears from the counter
    if counter < 1:
        return suffix_keys[0]  # If the counter is 1 or less, return the current suffix
    else:
        return recursive_choice(counter, suffix_dict, suffix_keys[1:])  # Else, call again recursively


def choose_word(chain, prefix, randomizer):  # With a selected prefix, randomly choose a suffix
    suffixes = chain.get(prefix)  # Get the ImmDict of suffixes for the selected prefix
    return recursive_choice(
        randomizer(reduce(lambda x, y: x+y, sorted(suffixes.values()))),  # Sum the number of times each suffix appears
        suffixes, list(suffixes.keys()))  # Return the sum as randomizer bound, the suffixes ImmDict, and keys as a list
