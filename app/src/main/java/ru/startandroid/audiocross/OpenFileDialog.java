package ru.startandroid.audiocross;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class OpenFileDialog extends AlertDialog.Builder {

    private String currentPath = Environment.getExternalStorageDirectory().getPath();
    private List<File> files = new ArrayList<File>();
    private FilenameFilter filenameFilter;
    private ListView listView;
    private int selectedIndex = -1;
    private OpenDialogListener listener;

    public OpenFileDialog(Context context) {
        super(context);
        files.addAll(getFiles(currentPath));
        listView = createListView(context);
        setView(listView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (selectedIndex > -1 && listener != null) {
                            listener.OnSelectedFile(listView.getItemAtPosition(selectedIndex).toString());
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
    }

    public interface OpenDialogListener {
        public void OnSelectedFile(String fileName);
    }

    public OpenFileDialog setOpenDialogListener(OpenDialogListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public AlertDialog show() {
        listView.setAdapter(new FileAdapter(getContext(), files));
        return super.show();
    }


    private List<File> getFiles(String directoryPath){
        File directory = new File(directoryPath);
        List<File> fileList = Arrays.asList(directory.listFiles(filenameFilter));
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                if (file.isDirectory() && file2.isFile())
                    return -1;
                else if (file.isFile() && file2.isDirectory())
                    return 1;
                else
                    return file.getPath().compareTo(file2.getPath());
            }
        });
        return  fileList;
    }

    public OpenFileDialog setFilter(final  String filter) {
        filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                File tempFile = new File(String.format("%s/%s", file.getPath(), s));
                if (tempFile.isFile())
                    return tempFile.getName().matches(filter);
                return true;
            }
        };
        return this;
    }


    private void RebuildFiles (ArrayAdapter<File> adapter) {
        List<File> fileList = getFiles(currentPath);
        files.clear();
        selectedIndex = -1;
        files.addAll(fileList);
        adapter.notifyDataSetChanged();
    }

    private ListView createListView(Context context) {
        ListView listView = new ListView(context);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final  ArrayAdapter<File> adapter = (FileAdapter) adapterView.getAdapter();
                File file = adapter.getItem(i);
                if (file.isDirectory()) {
                    currentPath = file.getPath();
                    RebuildFiles(adapter);
                } else {
                    if (i != selectedIndex)
                        selectedIndex = i;
                    else
                        selectedIndex = -1;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return listView;
    }


    public class FileAdapter extends ArrayAdapter<File> {
        public FileAdapter(Context context, List<File> files){
            super(context, android.R.layout.simple_expandable_list_item_1, files);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            File file = getItem(position);
            view.setText(file.getName());
            if (selectedIndex == position)
                view.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
            else
                view.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));

            return view;
        }
    }


}
