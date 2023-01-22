package org.hzt.coroutines.channel

import org.hzt.coroutines.context.CommonPool
import org.hzt.coroutines.run.runBlocking

fun mainBlocking(block: suspend () -> Unit) = runBlocking(CommonPool, block)

fun go(block: suspend () -> Unit) = CommonPool.runParallel(block)

