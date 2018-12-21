package com.example.vladislav.android5.Models

data class RSSObject(
        val status: String,
        val channel: Feed = Feed(),
        val items: List<Item>
)