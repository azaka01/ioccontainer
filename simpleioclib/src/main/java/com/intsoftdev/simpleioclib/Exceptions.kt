package com.intsoftdev.simpleioclib

class UnresolvedClassException(msg: String) : RuntimeException(msg)

class CircularDependencyException(msg: String) : RuntimeException(msg)