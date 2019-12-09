import prefix
import suffix
from functools import reduce


def get_word_list(chain, start_prefix, randomizer_func, wordcount, nonword):
    sentence = words_gen(chain, start_prefix, randomizer_func, wordcount, nonword)  # Return immutable list of words
    return tuple(sentence)


def generate(chain, randomizer, wordcount, nonword):  # Reduce the list of words into a sentence string
    return reduce(lambda x, y: str(x) + " " + str(y), get_word_list(chain, prefix.new_prefix(nonword, nonword),
                                                                    randomizer, wordcount, nonword))


def words_gen(chain, current_prefix, randomizer, counter, nonword):  # Recursively generate a random list of words
    new_word = suffix.choose_word(chain, current_prefix, randomizer)
    if new_word == nonword:
        return []  # If the next word is end of line, stop early
    elif counter == 1:
        return [new_word]  # Generate last word in sentence
    else:
        # Recursively generate new words until word count reached
        return [new_word] + words_gen(chain, prefix.shift_in(current_prefix, new_word), randomizer, counter-1, nonword)
