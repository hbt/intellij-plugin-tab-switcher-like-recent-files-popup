import com.intellij.ide.actions.ShowRecentlyEditedFilesAction;
import com.intellij.ide.actions.Switcher;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.tabs.JBTabs;
import org.apache.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmartTabSwitcher extends AnAction {
    private final static Logger logger = LoggerFactory.createLogger();

    @Override
    public void actionPerformed(AnActionEvent e) {
        logger.setLevel(Level.ALL);
        logger.info("--");

        Project project = e.getProject();
        if (project == null) {
            logger.debug("No project");
            return;
        }

        // TODO(hbt) NEXT add filter for same window only -- new action
//         current editor only
//        EditorWindow editorWindow = EditorWindow.DATA_KEY.getData(e.getDataContext());

        FileEditorManagerEx manager = FileEditorManagerEx.getInstanceEx(e.getProject());
        ArrayList<VirtualFile> tabFiles = new ArrayList<VirtualFile>();

        logger.debug("WINDOWS COUNT: " + manager.getWindows().length);

        for (EditorWindow editorWindow : manager.getWindows()) {
            JBTabs tabs = editorWindow.getTabbedPane().getTabs();
            VirtualFile[] files = editorWindow.getFiles();
            for (VirtualFile f : files) {
                if (tabs.findInfo(f) != null) {
                    tabFiles.add(f);
                }
            }
        }

        logger.debug("TABS COUNT: " + tabFiles.size());
        VirtualFile[] tabFiles2 = new VirtualFile[tabFiles.size()];
        tabFiles2 = tabFiles.toArray(tabFiles2);
        Switcher.SwitcherPanel recently_edited_files = Switcher.createAndShowSwitcher(e, "Current Tabs", true, tabFiles2);

        // Note(hbt) list count should be shorter if we have opened windows or split tabs as the "active/visible"  are not added to the list 
        logger.debug("LIST COUNT: " + recently_edited_files.getSelectedList().getItemsCount());
    }
}
