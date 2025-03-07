package org.sirix.axis.temporal;

import static com.google.common.base.Preconditions.checkNotNull;

import org.sirix.api.NodeCursor;
import org.sirix.api.NodeReadOnlyTrx;
import org.sirix.api.NodeTrx;
import org.sirix.api.ResourceManager;
import org.sirix.axis.AbstractTemporalAxis;

/**
 * Open the next revision and try to move to the node with the given node key.
 *
 * @author Johannes Lichtenberger
 *
 */
public final class NextAxis<R extends NodeReadOnlyTrx & NodeCursor, W extends NodeTrx & NodeCursor>
    extends AbstractTemporalAxis<R, W> {

  /** Sirix {@link ResourceManager}. */
  private final ResourceManager<R, W> resourceManager;

  /** Determines if it's the first call. */
  private boolean first;

  /** The revision number. */
  private int revision;

  /** Node key to lookup and retrieve. */
  private long nodeKey;

  /**
   * Constructor.
   *
   * @param rtx Sirix {@link NodeReadOnlyTrx}
   */
  public NextAxis(final ResourceManager<R, W> resourceManager, final R rtx) {
    this.resourceManager = checkNotNull(resourceManager);
    revision = 0;
    nodeKey = rtx.getNodeKey();
    revision = rtx.getRevisionNumber() + 1;
    first = true;
  }

  @Override
  protected R computeNext() {
    if (revision <= resourceManager.getMostRecentRevisionNumber() && first) {
      first = false;

      final R rtx = resourceManager.beginNodeReadOnlyTrx(revision);
      revision++;

      if (rtx.moveTo(nodeKey).hasMoved()) {
        return rtx;
      } else {
        rtx.close();
        return endOfData();
      }
    } else {
      return endOfData();
    }
  }

  @Override
  public ResourceManager<R, W> getResourceManager() {
    return resourceManager;
  }
}
