
package org.exist.eclipse.xquery.core.internal;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.dltk.core.ScriptContentDescriber;

/**
 * This is the description implementation of the xquery content type. It is registered as an extension.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryContentDescriber extends ScriptContentDescriber {

  public XQueryContentDescriber() {
  }

  @Override
public int describe(Reader contents, IContentDescription description) throws IOException {
    return IContentDescriber.VALID;
  }

  @Override
  protected Pattern[] getHeaderPatterns() {
    return new Pattern[] {};
  }
}
