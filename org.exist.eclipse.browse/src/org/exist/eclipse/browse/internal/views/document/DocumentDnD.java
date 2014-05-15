package org.exist.eclipse.browse.internal.views.document;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.internal.create.ImportDocumentsListener;

public class DocumentDnD {

	private static final String BASE_UUID = UUID.randomUUID().toString();
	private static int _tmpSessionCounter;

	private static class DropTargetListener extends DropTargetAdapter {
		private final DocumentView _view;
		private final DragSourceListener _dragSource;

		private DropTargetListener(DocumentView view,
				DragSourceListener dragSource) {
			_view = view;
			_dragSource = dragSource;
		}

		@Override
		public void drop(DropTargetEvent event) {

			if (!_view.hasItem() || event.data == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			boolean sourceIsSelf = false;
			List<File> files = new ArrayList<>();

			// check for dropped on/dragged from self
			String session = _dragSource.getTmpSession();
			for (String it : (String[]) event.data) {
				if (!session.isEmpty() && it.contains(session)) {
					sourceIsSelf = true;
					break;
				}
				File f = new File(it);
				if (f.isFile()) {
					files.add(f);
				}
			}

			if (sourceIsSelf) {
				event.detail = DND.DROP_NONE;
			} else {
				new ImportDocumentsListener().importFiles(_view.getItem(),
						files);
				event.detail = DND.DROP_COPY;
			}
		}
	}

	private static class DragSourceListener extends DragSourceAdapter {

		private final TableViewer _viewer;
		private String[] _tmpFiles;
		private File _tmpSessionDir;
		private String _tmpSession;

		private DragSourceListener(TableViewer viewer) {
			_viewer = viewer;
		}

		@Override
		public void dragStart(DragSourceEvent event) {
			_tmpSession = BASE_UUID + _tmpSessionCounter;
			File tmpDir = new File(System.getProperty("java.io.tmpdir", "."));
			_tmpSessionDir = new File(tmpDir, _tmpSession);
			_tmpSessionDir.mkdirs();
			_tmpFiles = null;
			_tmpSessionCounter++;
		}

		protected String getTmpSession() {
			if (_tmpSession == null) {
				_tmpSession = "";
			}
			return _tmpSession;
		}

		@Override
		public void dragSetData(DragSourceEvent event) {
			try {
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					StringWriter out = new StringWriter();
					PrintWriter pw = new PrintWriter(out);

					for (Object it : ((IStructuredSelection) _viewer
							.getSelection()).toArray()) {
						pw.println(((IDocumentItem) it).getResource()
								.getContent());
					}

					event.data = out.toString();
				} else if (FileTransfer.getInstance().isSupportedType(
						event.dataType)) {
					if (_tmpFiles == null) {
						_tmpFiles = createTempFiles();
					}
					event.data = _tmpFiles;
				}
			} catch (Exception e) {
				File sessionDir = _tmpSessionDir;
				reset();
				deleteRecursive(sessionDir);
				throw new RuntimeException(e);
			}
		}

		private void reset() {
			_tmpFiles = null;
			_tmpSessionDir = null;
			_tmpSession = null;
		}

		@Override
		public void dragFinished(DragSourceEvent event) {
			// without delay, when dropping into Eclipse, an error occurs
			// like "File XY does not exist"

			final File sessionDir = _tmpSessionDir;
			reset();

			event.display.timerExec(3000, new Runnable() {
				@Override
				public void run() {
					deleteRecursive(sessionDir);
				}
			});
		}

		private String[] createTempFiles() {
			List<File> files = new ArrayList<>();

			for (Object it : ((IStructuredSelection) _viewer.getSelection())
					.toArray()) {
				IDocumentItem di = (IDocumentItem) it;
				File tmpDir = new File(_tmpSessionDir, di.getParent().getPath()
						.replace('/', '_'));

				tmpDir.mkdirs();

				File tmpFile = new File(tmpDir, di.getName());
				files.add(tmpFile);

				if (!tmpFile.exists()) {
					try {
						tmpFile.deleteOnExit();

						ExportDocumentsAction.copy(new StringReader((String) di
								.getResource().getContent()), new FileWriter(
								tmpFile));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}

			List<String> result = new ArrayList<>(files.size());
			for (File it : files) {
				result.add(it.getAbsolutePath());
			}

			return result.toArray(new String[result.size()]);
		}

		public boolean deleteRecursive(File path) {
			if (!path.exists()) {
				return false;
			}
			boolean ret = true;
			if (path.isDirectory()) {
				for (File f : path.listFiles()) {
					ret = ret && deleteRecursive(f);
				}
			}
			return ret && path.delete();
		}
	}

	public static void install(final DocumentView view) {

		final TableViewer viewer = view.getViewer();

		int operations = DND.DROP_MOVE | DND.DROP_COPY;
		final DropTarget target = new DropTarget(viewer.getTable(), operations);
		target.setTransfer(new Transfer[] { FileTransfer.getInstance() });

		DragSource source = new DragSource(viewer.getTable(), operations);
		source.setTransfer(new Transfer[] { TextTransfer.getInstance(),
				FileTransfer.getInstance() });

		DragSourceListener dragSourceListener = new DragSourceListener(viewer);
		source.addDragListener(dragSourceListener);
		target
				.addDropListener(new DropTargetListener(view,
						dragSourceListener));
	}
}
