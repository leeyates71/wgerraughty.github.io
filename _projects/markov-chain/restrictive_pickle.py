import io
import pickle
import immdict


# Restricted Unpickler from Python Documentation (https://docs.python.org/3/library/pickle.html#restricting-globals)
class RestrictedUnpickler(pickle.Unpickler):

    def find_class(self, module, name):
        # Only allow ImmDict.
        if module == "immdict" and name == "ImmDict":
            return getattr(immdict, name)
        # Forbid everything else.
        raise pickle.UnpicklingError("global '%s.%s' is forbidden" %
                                     (module, name))


def restricted_loads(s):
    """Helper function analogous to pickle.loads()."""
    return RestrictedUnpickler(io.BytesIO(s)).load()
