package org.sirix.xquery.json;

import com.google.common.base.Preconditions;
import org.brackit.xquery.atomic.IntNumeric;
import org.brackit.xquery.xdm.Stream;
import org.brackit.xquery.xdm.json.Array;
import org.sirix.api.json.JsonNodeReadOnlyTrx;
import org.sirix.axis.IncludeSelf;
import org.sirix.axis.temporal.AllTimeAxis;
import org.sirix.axis.temporal.FutureAxis;
import org.sirix.axis.temporal.PastAxis;
import org.sirix.xquery.stream.json.TemporalSirixJsonObjectValueArrayStream;

public final class JsonObjectValueDBArray extends AbstractJsonDBArray<JsonObjectValueDBArray>
    implements TemporalJsonDBItem<JsonObjectValueDBArray> {

  /**
   * Sirix read-only transaction.
   */
  private final JsonNodeReadOnlyTrx rtx;

  /**
   * Collection this node is part of.
   */
  private final JsonDBCollection collection;

  /**
   * Constructor.
   *
   * @param rtx        {@link JsonNodeReadOnlyTrx} for providing reading access to the underlying node
   * @param collection {@link JsonDBCollection} reference
   */
  public JsonObjectValueDBArray(final JsonNodeReadOnlyTrx rtx, final JsonDBCollection collection) {
    super(rtx, collection, new JsonItemFactory());
    this.collection = Preconditions.checkNotNull(collection);
    this.rtx = Preconditions.checkNotNull(rtx);
    assert this.rtx.isObject();
  }

  @Override
  public Stream<JsonObjectValueDBArray> getEarlier(final boolean includeSelf) {
    moveRtx();
    final IncludeSelf include = includeSelf ? IncludeSelf.YES : IncludeSelf.NO;
    return new TemporalSirixJsonObjectValueArrayStream(new PastAxis<>(rtx.getResourceManager(), rtx, include),
                                                       collection);
  }

  @Override
  public Stream<JsonObjectValueDBArray> getFuture(final boolean includeSelf) {
    moveRtx();
    final IncludeSelf include = includeSelf ? IncludeSelf.YES : IncludeSelf.NO;
    return new TemporalSirixJsonObjectValueArrayStream(new FutureAxis<>(rtx.getResourceManager(), rtx, include),
                                                       collection);
  }

  @Override
  public Stream<JsonObjectValueDBArray> getAllTimes() {
    moveRtx();
    return new TemporalSirixJsonObjectValueArrayStream(new AllTimeAxis<>(rtx.getResourceManager(), rtx), collection);
  }

  @Override
  protected JsonObjectValueDBArray createInstance(JsonNodeReadOnlyTrx rtx, JsonDBCollection collection) {
    return new JsonObjectValueDBArray(rtx, collection);
  }

  @Override
  public Array range(IntNumeric from, IntNumeric to) {
    moveRtx();

    return new JsonDBArraySlice(rtx, collection, from.intValue(), to.intValue());
  }
}
