package by.imag.app;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import by.imag.app.classes.ArchiveItem;
import by.imag.app.classes.Constants;
import by.imag.app.classes.DocumentParser;

public class FragmentArchives extends BaseFragment {
    private GridView gridView;
    private ProgressBar pbArch;
    private AppDb appDb;
    private Cursor cursor;
    private ArchCursorAdapter cursorAdapter;
    private SharedPreferences preferences;
    private boolean update;
//    private String subtitle;
    private ArchLoader archLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_arch, container, false);
        appDb = new AppDb(getActivity().getApplicationContext());
        gridView = (GridView) rootView.findViewById(R.id.gridArch);
        pbArch = (ProgressBar) rootView.findViewById(R.id.pbArch);
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        loadPreferences();
        archLoader = new ArchLoader();
        archLoader.execute();
//        subtitle = getResources().getStringArray(R.array.menu_items)[2];
//        getActivity().getActionBar().setSubtitle(subtitle);
        setActionBarSubtitle(2);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        archLoader.cancel(true);
    }

    private void loadPreferences() {
        update = preferences.getBoolean(Constants.UPDATE_ARCHIVES, true);
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.UPDATE_ARCHIVES, false);
        editor.commit();
    }

    private void setView() {
        cursor = appDb.getArchCursor();
        cursorAdapter = new ArchCursorAdapter(getActivity(), cursor, true);
        gridView.setAdapter(cursorAdapter);
        archiveClick();
    }

    private void archiveClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long _id) {
                ArchiveItem archiveItem = appDb.getArchiveItem(_id);
//                String archName = archiveItem.getArchName();
//                String archUrl = archiveItem.getArchUrl();
//                archUrl = archUrl + "&paged=1";
//                Fragment testFragment = new FragmentPosts(archiveItem);
                Fragment testFragment = FragmentPosts.newInstance(archiveItem);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.content_frame, testFragment);
                transaction.commit();
            }
        });
    }


    private class ArchLoader extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbArch.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean archUpdated = false;
            if (isOnline() && update) {
                DocumentParser documentParser = new DocumentParser(Constants.PAGE + 1);
                List<ArchiveItem> archives = documentParser.getArchives();
                archUpdated = appDb.writeArchivesTable(archives);
            }
            return archUpdated;
        }

        @Override
        protected void onPostExecute(Boolean archUpdated) {
            super.onPostExecute(archUpdated);
//            logMsg("arch updated: "+archUpdated);
            if (archUpdated) {
                if (isAdded()) {
                    setView();
                }
                savePreferences();
            }
            pbArch.setVisibility(View.GONE);
        }
    }

    private class ArchCursorAdapter extends CursorAdapter {

        public ArchCursorAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.arch_item, viewGroup, false);
            bindView(view, context, cursor);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvArch = (TextView) view.findViewById(R.id.tvArch);
            String archName = cursor.getString(cursor.getColumnIndex(AppDb.ARCH_NAME));
//            logMsg("arch name: "+archName);
            tvArch.setText(archName);
        }
    }
}
