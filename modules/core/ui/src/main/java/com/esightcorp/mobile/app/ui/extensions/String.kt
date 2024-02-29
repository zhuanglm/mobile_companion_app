/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.extensions

/**
 * Ignore the [skip] text segment from the input string while narration.
 *
 * @param skip Segment that will not be narrated
 * @return The updated string for accessibility (talkback) service
 */
fun String.narratorSkip(skip: String) = this.replace(skip, "-")
