package com.example.interface3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private List<Map<String, Object>> data;
    private OnItemClickListener listener;
    private static final String TAG = "CustomAdapter";

    public CustomAdapter(Context context,List<Map<String, Object>> data, OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    public void updateData(List<Map<String, Object>> newData) {
        data.clear();

        for (Map<String, Object> map : newData) {
            Applicant applicant = new Applicant(
                    (String) map.get("firstName"),
                    (String) map.get("lastName"),
                    (String) map.get("middleName"),
                    (int) map.get("eGE"),
                    (int) map.get("priority"),
                    (String) map.get("profile"),
                    (String) map.get("id"),
                    (List<String>) map.get("statuses")
            );

            Map<String, Object> applicantMap = new LinkedHashMap<>();
            applicantMap.put("firstName", applicant.getFirstName());
            applicantMap.put("lastName", applicant.getLastName());
            applicantMap.put("middleName", applicant.getMiddleName());
            applicantMap.put("eGE", applicant.geteGE());
            applicantMap.put("priority", applicant.getPriority());
            applicantMap.put("profile", applicant.getProfile());
            applicantMap.put("status", applicant.getStatuses());
            data.add(applicantMap);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.znac_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (data == null || data.isEmpty() || position < 0 || position >= data.size()) {
            return;
        }

        Map<String, Object> map = data.get(position);

        if (map == null || map.isEmpty()) {
            return;
        }
        ArrayList<String> statuses = (ArrayList<String>) map.get("statuses");
        List<String> statusesList;
        if (statuses != null) {
            statusesList = new ArrayList<>(statuses);
        } else {
            statusesList = new ArrayList<>();
        }
        Applicant item = new Applicant(
                (String) map.get("firstName"),
                (String) map.get("lastName"),
                (String) map.get("middleName"),
                (int) map.get("eGE"),
                (int) map.get("priority"),
                (String) map.get("profile"),
                (String) map.get("id"),
                statusesList
        );

        holder.nameTextView.setText(item.getLastName() + " " + item.getFirstName() + " " + item.getMiddleName());
        holder.scoreTextView.setText(String.valueOf(item.geteGE()));
        holder.priorityTextView.setText(String.valueOf(item.getPriority()));
        holder.profileTextView.setText(item.getProfile());

        Drawable defaultImage = ContextCompat.getDrawable(context, R.drawable.etstatus);
        Drawable statusDrawable = null;

        for (String status : item.getStatuses()) {
            Drawable imageResource = item.getImageResource(context, Collections.singletonList(status));

            if (imageResource != null) {
                statusDrawable = imageResource;
                break;
            }
        }

        holder.imageView.setImageDrawable(statusDrawable != null ? statusDrawable : defaultImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    ApplicantSearchTask searchTask = new ApplicantSearchTask(new ApplicantSearchTask.SearchResponseListener() {
                        @Override
                        public void onApplicantFound(String applicantId) {
                            if (applicantId != null) {
                                Intent intent = new Intent(context, FullApplicant.class);
                                intent.putExtra("applicantId", applicantId);
                                context.startActivity(intent);
                            } else {
                                Log.d(TAG, "Applicant not found.");
                            }
                        }
                    }, item);

                    searchTask.execute();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView nameTextView;
        TextView scoreTextView;
        TextView priorityTextView;
        TextView profileTextView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            profileTextView = itemView.findViewById(R.id.profileTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Applicant applicant);
    }
}