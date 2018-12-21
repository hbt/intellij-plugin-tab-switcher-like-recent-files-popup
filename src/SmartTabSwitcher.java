import com.intellij.ide.actions.MySwitcher;
import com.intellij.ide.actions.Switcher;
import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.tabs.JBTabs;
import org.apache.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class SmartTabSwitcher extends AnAction {
    private final static Logger logger = LoggerFactory.createLogger();

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO(hbt) NEXT res
        logger.setLevel(Level.ALL);

        Project project = e.getProject();
        if (project == null) {
            logger.debug("No project");
            return;
        }

        // TODO(hbt) ENHANCE add filter for same window only -- new action
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

        boolean displayAllTabs = true;

//        List<VirtualFile> fileList = EditorHistoryManager.getInstance(project).getFileList();
//        VirtualFile[] tabFiles3 = new VirtualFile[fileList.size()];
//        tabFiles3 = fileList.toArray(tabFiles3);
        
        if(displayAllTabs)
        {
            // Note(hbt) hack to display across windows
            int editorTabPlacement = UISettings.getInstance().getEditorTabPlacement();
            UISettings.getInstance().setEditorTabPlacement(UISettings.TABS_NONE);
//        MySwitcher.SwitcherPanel recently_edited_files = MySwitcher.createAndShowSwitcher(e, "Switcher", true, tabFiles2);
            Switcher.SwitcherPanel recently_edited_files = Switcher.createAndShowSwitcher(e, "Current Tabs", true, tabFiles2);
            UISettings.getInstance().setEditorTabPlacement(editorTabPlacement);
            
        }
        else
        {
            // Note(hbt) displays tabs and filters active editors
            Switcher.SwitcherPanel recently_edited_files = Switcher.createAndShowSwitcher(e, "Current Tabs", true, tabFiles2);
        }
        


//        Switcher.SwitcherPanel recently_edited_files = Switcher.createAndShowSwitcher(e, "Switcher", true, tabFiles3);

        // Note(hbt) list count should be shorter if we have opened windows or split tabs as the "active/visible"  are not added to the list 
//        logger.debug("LIST COUNT: " + recently_edited_files.getSelectedList().getItemsCount());
    }
}
