package com.android.xrayfa.webapp

import com.android.xrayfa.model.Node
import com.android.xrayfa.model.Subscription
import com.android.xrayfa.model.SubscriptionMeta
import com.android.xrayfa.repository.NodeRepository
import com.android.xrayfa.repository.SubscriptionRepository
import com.android.xrayfa.shared.platform.ClipboardReader
import com.android.xrayfa.shared.platform.ClipboardWriter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class WasmNodeRepository : NodeRepository {
    private val _nodes = MutableStateFlow<List<Node>>(emptyList())
    override val allNodes: Flow<List<Node>> = _nodes.asStateFlow()

    override val favorites: Flow<List<Node>> = _nodes.map { list -> list.filter { it.favorite } }

    override suspend fun addNode(vararg nodes: Node) {
        val current = _nodes.value.toMutableList()
        nodes.forEach { node ->
            val nextId = if (node.id <= 0) (current.maxOfOrNull { it.id } ?: 0) + 1 else node.id
            val newIndex = current.indexOfFirst { it.id == nextId }
            val nodeWithId = node.copy(id = nextId)
            if (newIndex >= 0) {
                current[newIndex] = nodeWithId
            } else {
                current.add(nodeWithId)
            }
        }
        _nodes.value = current
    }

    override suspend fun deleteLink(link: Node) {
        _nodes.value = _nodes.value.filter { it.id != link.id }
    }

    override fun loadLinksById(id: Int): Flow<Node?> {
        return _nodes.map { list -> list.firstOrNull { it.id == id } }
    }

    override suspend fun clearSelection() {
        _nodes.value = _nodes.value.map { it.copy(selected = false) }
    }

    override fun querySelectedNode(): Flow<Node?> {
        return _nodes.map { list -> list.firstOrNull { it.selected } }
    }

    override fun queryPreNode(): Flow<Node?> = _nodes.map { null }
    override fun queryNextNode(): Flow<Node?> = _nodes.map { null }

    override suspend fun updateNode(id: Int, url: String, port: Int, remark: String?) {
        _nodes.value = _nodes.value.map {
            if (it.id == id) it.copy(url = url, port = port, remark = remark) else it
        }
    }

    override suspend fun updateSelectById(id: Int, selected: Boolean) {
        _nodes.value = _nodes.value.map {
            if (it.id == id) it.copy(selected = selected) else if (selected) it.copy(selected = false) else it
        }
    }

    override suspend fun updateFavoriteById(id: Int, favorite: Boolean) {
        _nodes.value = _nodes.value.map {
            if (it.id == id) it.copy(favorite = favorite) else it
        }
    }

    override suspend fun deleteLinkById(id: Int) {
        _nodes.value = _nodes.value.filter { it.id != id }
    }

    override suspend fun deleteLinkBySubscriptionId(subscriptionId: Int) {
        _nodes.value = _nodes.value.filter { it.subscriptionId != subscriptionId }
    }

    override suspend fun deleteAllNodes() {
        _nodes.value = emptyList()
    }
}

class WasmSubscriptionRepository : SubscriptionRepository {
    private val _subscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    override val allSubscriptions: Flow<List<Subscription>> = _subscriptions.asStateFlow()

    override suspend fun addSubscription(subscription: Subscription): Long {
        val current = _subscriptions.value.toMutableList()
        val nextId = if (subscription.id <= 0) (current.maxOfOrNull { it.id } ?: 0) + 1 else subscription.id
        val subWithId = subscription.copy(id = nextId)
        current.add(subWithId)
        _subscriptions.value = current
        return nextId.toLong()
    }

    override suspend fun deleteSubscription(subscription: Subscription) {
        _subscriptions.value = _subscriptions.value.filter { it.id != subscription.id }
    }

    override suspend fun updateSubscription(subscription: Subscription) {
        _subscriptions.value = _subscriptions.value.map {
            if (it.id == subscription.id) subscription else it
        }
    }

    override fun getSubscriptionById(id: Int): Flow<Subscription?> {
        return _subscriptions.map { list -> list.firstOrNull { it.id == id } }
    }

    override suspend fun fetchAndSaveNodes(
        url: String,
        subscriptionId: Int,
        extraHeaders: Map<String, String>,
    ): SubscriptionMeta {
        return SubscriptionMeta(
            announce = null,
            profileTitle = null,
            profileUpdateIntervalHours = null,
            profileWebPageUrl = null,
            routing = null,
            routingEnable = null,
            supportUrl = null,
            servedBy = null,
            userInfo = null,
        )
    }
}

class WasmClipboardReader : ClipboardReader {
    private var text = ""
    override fun readText(): String = text
    fun setText(newText: String) { text = newText }
}

class WasmClipboardWriter : ClipboardWriter {
    override fun writeText(text: String) {}
}
