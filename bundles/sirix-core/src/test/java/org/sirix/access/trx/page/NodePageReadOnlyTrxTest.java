package org.sirix.access.trx.page;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.sirix.access.ResourceConfiguration;
import org.sirix.access.trx.node.InternalResourceManager;
import org.sirix.cache.BufferManager;
import org.sirix.cache.TransactionIntentLog;
import org.sirix.index.IndexType;
import org.sirix.io.Reader;
import org.sirix.page.UberPage;
import org.sirix.settings.Constants;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class NodePageReadOnlyTrxTest {

  @Test
  public void testPageKey() {
    final InternalResourceManager resourceManagerMock = createResourceManagerMock();

    final var trx = new NodePageReadOnlyTrx(1, resourceManagerMock, new UberPage(), 0,
                                            mock(Reader.class), mock(TransactionIntentLog.class), mock(BufferManager.class),
                                            mock(RevisionRootPageReader.class));

    assertEquals(0, trx.pageKey(1, IndexType.DOCUMENT));
    assertEquals(1023 / Constants.NDP_NODE_COUNT, trx.pageKey(1023, IndexType.DOCUMENT));
    assertEquals(1024 / Constants.NDP_NODE_COUNT, trx.pageKey(1024, IndexType.DOCUMENT));
  }

  @Test
  public void testRecordPageOffset() {
    final InternalResourceManager resourceManagerMock = createResourceManagerMock();

    final var trx = new NodePageReadOnlyTrx(1, resourceManagerMock, new UberPage(), 0,
        mock(Reader.class), mock(TransactionIntentLog.class), mock(BufferManager.class),
        mock(RevisionRootPageReader.class));

    assertEquals(1, trx.recordPageOffset(1));
    assertEquals(Constants.NDP_NODE_COUNT - 1, trx.recordPageOffset(1023));
  }

  @NotNull
  private InternalResourceManager createResourceManagerMock() {
    final var resourceManagerMock = mock(InternalResourceManager.class);
    when(resourceManagerMock.getResourceConfig()).thenReturn(new ResourceConfiguration.Builder("foobar").build());
    return resourceManagerMock;
  }
}