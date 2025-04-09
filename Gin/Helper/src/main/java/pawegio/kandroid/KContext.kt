/*
 * Copyright 2015-2016 Paweł Gajda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE")

package com.pawegio.kandroid
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

inline fun Context.toast(text: CharSequence): Toast = Toast.makeText(this, text, Toast.LENGTH_SHORT).apply { show() }

inline fun Context.longToast(text: CharSequence): Toast = Toast.makeText(this, text, Toast.LENGTH_LONG).apply { show() }

inline fun Context.toast(@StringRes resId: Int): Toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT).apply { show() }

inline fun Context.longToast(@StringRes resId: Int): Toast = Toast.makeText(this, resId, Toast.LENGTH_LONG).apply { show() }