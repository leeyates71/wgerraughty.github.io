def new_prefix(first_word, second_word):
    return tuple([first_word, second_word])  # Return a two-word prefix


def shift_in(prefix, new_word):
    return tuple([prefix[1], new_word])  # Create a new prefix using second value in the original prefix and a new word
