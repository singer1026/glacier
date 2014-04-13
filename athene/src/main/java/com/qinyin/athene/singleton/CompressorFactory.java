/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-3-28
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.singleton;

import com.google.javascript.jscomp.CompilationLevel;
import com.googlecode.htmlcompressor.compressor.ClosureJavaScriptCompressor;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class CompressorFactory {

    public static HtmlCompressor getCompressor() {
        HtmlCompressor compressor = new HtmlCompressor();
        ClosureJavaScriptCompressor javaScriptCompressor = new ClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS);
        compressor.setJavaScriptCompressor(javaScriptCompressor);
        compressor.setCompressJavaScript(true);
        compressor.setCompressCss(true);
        return compressor;
    }
}
