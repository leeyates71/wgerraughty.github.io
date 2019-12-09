class ImmDict:  # ImmDict, an immutable implentation of a Python dictionary

    def __init__(self, old_dict=None):
        self.idict = {}  # Default constructor for Immutable Dictionary
        if isinstance(old_dict, dict):
            self.idict = old_dict.copy()  # Copy constructor

    def put(self, key, value):  # Create a new ImmDict using updated values
        x = ImmDict()
        x.idict = self.idict.copy()
        x.idict.update({key: value})
        return x

    def get(self, key):  # Python Dictionary get function
        return self.idict.get(key)

    def keys(self):  # Python Dictionary keys function
        return list(self.idict.keys())

    def values(self):  # Python Dictionary values function
        return list(self.idict.values())
