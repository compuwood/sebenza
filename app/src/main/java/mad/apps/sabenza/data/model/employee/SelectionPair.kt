package mad.apps.sabenza.data.rpc.calls.employee

class SelectionPair(val id : String, val description : String) {
    override fun toString(): String {
        return id.toString() + "," + description;
    }
}