package com.example.stylishsloth.helper_classes;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stylishsloth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class RecyclerViewQuestionsAdapter extends RecyclerView.Adapter<RecyclerViewQuestionsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cardText;
        public ImageView imageView;
        public Button messageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            cardText = itemView.findViewById(R.id.cardText);
            imageView = itemView.findViewById(R.id.imageView4);

        }
    }

    private List<StoriesItem> items;

    public RecyclerViewQuestionsAdapter(List<StoriesItem> mitems) {
        items = mitems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.stories_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StoriesItem item = items.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.cardText;
        textView.setText(item.getText());
        ImageView iv = holder.imageView;
        iv.setImageResource(item.getImage());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DocumentReference docRef = db.collection("AppData").document("Articles");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                //ВЫЗОВ ДИАЛОГОВОГО ОКНА
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.CustomAlertDialog);
                                View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.dialog_fragment, null);
                                TextView title = dialogView.findViewById(R.id.title_textview);
                                TextView text = dialogView.findViewById(R.id.text_textview);
                                HashMap<String, String> article = (HashMap<String, String>) document.get(String.valueOf(position+1));
                                title.setText(article.get("Title"));
                                text.setText(article.get("Text"));

                                builder.setView(dialogView);

                                builder.setCancelable(true);

                                builder.show();


                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }

                    }
                });

            }
        });
    }



    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return items.size();
    }
}
