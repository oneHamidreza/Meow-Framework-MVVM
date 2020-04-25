---


---

<h2 id="🚂-system-extensions">🚂 System Extensions</h2>
<p>Use <code>getDeviceModel()</code> to get model of Device as a String.</p>
<p>Use <code>Context?.isPackageInstalled(packageName)</code> to check a package name is installed or not.</p>
<p>Use <code>Context?.getIMEI()</code> to get IMEI of Device as a String.</p>
<p>Use <code>Context?.getPhoneNumber()</code> to get Phone Number (if it exists) of Device by sim card information as a String. You’ll need to add <code>android.permission.READ_PHONE_STATE</code> in <code>AndroidManifest.xml</code>.</p>
<p>Use <code>Context?.getCountryCode()</code> to get Country Code (if it exists) of Device as a String.</p>
<p>Use <code>Context?.getDisplaySize()</code> to get size of Device’s Display as a Point.</p>
<p>Use <code>Context?.getToolbarHeight()</code> to get height of Toolbar as a Int.</p>
<p>Use <code>Context?.getStatusBarHeight()</code> to get height of Status as a Int.</p>
<p>Use <code>Float.dp()</code> or <code>Int.dp()</code> to get a value of float or int into dip unit. See this example :</p>
<pre class=" language-kotlin"><code class="prism  language-kotlin"><span class="token keyword">fun</span> <span class="token function">testDisplayMetricsDP</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
    <span class="token comment">// Returns 2f * app.resources.displayMetrics.density. If device has xxhdpi density (factor = 3) , the value of `someDp` will be `6f`.</span>
    <span class="token keyword">val</span> someDp <span class="token operator">=</span> <span class="token number">2f</span><span class="token punctuation">.</span><span class="token function">dp</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
    <span class="token keyword">val</span> someDpInt <span class="token operator">=</span> <span class="token number">2</span><span class="token punctuation">.</span><span class="token function">dp</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
<span class="token punctuation">}</span>
</code></pre>
<p>Use <code>Float.px()</code> or <code>Int.px()</code> to get a value of float or int (dip) into px unit. See this example :</p>
<pre class=" language-kotlin"><code class="prism  language-kotlin"><span class="token keyword">fun</span> <span class="token function">testDisplayMetricsPX</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
    <span class="token comment">// Returns 6f / app.resources.displayMetrics.density. If device has xxhdpi density (factor = 3) , the value of `someDp` will be `2f`.</span>
    <span class="token keyword">val</span> somePx <span class="token operator">=</span> <span class="token number">6f</span><span class="token punctuation">.</span><span class="token function">dp</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
    <span class="token keyword">val</span> somePxInt <span class="token operator">=</span> <span class="token number">6</span><span class="token punctuation">.</span><span class="token function">dp</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
<span class="token punctuation">}</span>
</code></pre>
<blockquote>
<p>You must bind your Application class with <code>meow.controller</code> in Application <code>onCreate()</code> to use above example.</p>
</blockquote>
<p>Use <code>Context?.isNightModeFromSettings()</code> to check that Night/Dark mode is enabled by System or not.</p>
<p>Use <code>Context?.showOrHideKeyboard()</code> to show or hide soft Keyboard.</p>
<p>Use <code>Context?.vibrate(duration)</code> to vibrate Device for specific duration time in millisecond. Default value of <code>duration</code> is 150.</p>

