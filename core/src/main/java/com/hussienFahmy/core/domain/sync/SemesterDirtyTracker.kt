package com.hussienfahmy.core.domain.sync

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Tracks whether semester data has been modified since the last sync.
 * Called by mutation use cases to mark changes, and consumed by screen-exit sync to decide
 * whether to push to Firebase.
 */
class SemesterDirtyTracker {
    private val _hasChanges = AtomicBoolean(false)

    fun markChanged() {
        _hasChanges.set(true)
    }

    /**
     * Returns true if changes occurred since the last call, and resets the flag.
     */
    fun consumeChanges(): Boolean = _hasChanges.getAndSet(false)
}
