package com.example.stylishsloth.helper_classes;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stylishsloth.R;
import com.example.stylishsloth.activities.ProfileActivity;
import com.example.stylishsloth.entities.Subscription;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewSubscriptionsAdapter extends RecyclerView.Adapter<RecyclerViewSubscriptionsAdapter.ViewHolder> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardTitle, topSizeTextView, pantsSizeTextView, shoesSizeTextView, adressTextView, statusTextView;
        public Button changeSubscriptionBtn, deleteSubscriptionBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.subscribeType);
            changeSubscriptionBtn = itemView.findViewById(R.id.changeSubscriptionBtn);
            topSizeTextView = itemView.findViewById(R.id.topSizeTextView);
            pantsSizeTextView = itemView.findViewById(R.id.pantsSizeTextView);
            shoesSizeTextView = itemView.findViewById(R.id.shoesSizeTextView);
            adressTextView = itemView.findViewById(R.id.adressTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            deleteSubscriptionBtn = itemView.findViewById(R.id.deleteSubscriptionBtn);
        }
    }

    private List<Subscription> items;
    String userId;
    ArrayList<String> sizes = new ArrayList<>(Arrays.asList("XS", "S", "M", "L", "XL", "XXL"));
    ArrayList<String> shoe_sizes = new ArrayList<>(Arrays.asList("36", "37", "38", "39", "40", "41", "42", "43", "44", "45"));

    // Pass in the contact array into the constructor
    public RecyclerViewSubscriptionsAdapter(List<Subscription> mitems, String userID) {
        items = mitems;
        userId = userID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View subscriptionView = inflater.inflate(R.layout.res_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(subscriptionView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //инициализация элементов
        TextView SubscribeType = holder.cardTitle;
        TextView TopSizeTV = holder.topSizeTextView;
        TextView PantsSizeTV = holder.pantsSizeTextView;
        TextView ShoesSizeTV = holder.shoesSizeTextView;
        TextView AdressTv = holder.adressTextView;
        TextView StatusTV = holder.statusTextView;
        Button deleteButton = holder.deleteSubscriptionBtn;
        SubscribeType.setText(items.get(position).getType());
        TopSizeTV.setText("Размер верха: " + items.get(position).getTopSize());
        PantsSizeTV.setText("Размер низа: " + items.get(position).getBottomSize());
        ShoesSizeTV.setText("Размер обуви: " + items.get(position).getFootSize());
        AdressTv.setText("Адрес: " + items.get(position).getAdress());
        if (items.get(position).isPayed()) {
            StatusTV.setText("Статус: оплачена");
        } else {
            StatusTV.setText("Статус: в обработке");
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Отказ от подписки");  // заголовок
                builder.setMessage("Вы действительно хотите отказаться от подписки?"); // сообщение

                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        db.collection("UsersData").document(userId)
                                .collection("Subscribes")
                                .document(items.get(position).getId())
                                .delete().addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error deleting document", e);
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                        Toast.makeText(v.getContext(), "Подписка отменена",
                                                Toast.LENGTH_LONG).show();

                                        ArrayList<Subscription> subscriptions = new ArrayList<>();
                                        Task<QuerySnapshot> task = db.collection("UsersData")
                                                .document(userId)
                                                .collection("Subscribes")
                                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                Log.d("TAG", document.getId() + " => " + document.getData());
                                                                Subscription subscription = new Subscription(
                                                                        document.getId(),
                                                                        document.get("Type").toString(),
                                                                        document.get("TopSize").toString(),
                                                                        document.get("PantsSize").toString(),
                                                                        document.get("FootSize").toString(),
                                                                        document.get("Adress").toString(),
                                                                        document.get("Payment").toString()
                                                                );

                                                                subscriptions.add(subscription);
                                                            }
                                                            items = subscriptions;
                                                            notifyDataSetChanged();
                                                        }
                                                    }
                                                });
                                    }
                                });
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.setCancelable(true);


            }
        });


        Button changeSubscriptionBtn = holder.changeSubscriptionBtn;

        changeSubscriptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.edit_subscription_data_dialog, null);
                //Создаем AlertDialog
                androidx.appcompat.app.AlertDialog.Builder mDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(v.getContext(), R.style.CustomAlertDialog);

                //инициализируем элементы упроавления на диалоговом окне
                AutoCompleteTextView topSizesDropDown = promptsView.findViewById(R.id.top_sizes_dropdown);
                AutoCompleteTextView PantsSizesDropDown = promptsView.findViewById(R.id.pants_sizes_dropdown);
                AutoCompleteTextView ShoesSizesDropDown = promptsView.findViewById(R.id.foot_sizes_dropdown);
                TextInputEditText AdressEditText = promptsView.findViewById(R.id.adressET);
                TextView subscriptionTitle = promptsView.findViewById(R.id.subscriptionTitle);

                //заполняем данные на диалоговом окне
                subscriptionTitle.setText(items.get(position).getType());

                ArrayAdapter<String> sizesAdapter = new ArrayAdapter(promptsView.getContext(), android.R.layout.simple_spinner_item, sizes);
                sizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                topSizesDropDown.setText(items.get(position).getTopSize());
                topSizesDropDown.setAdapter(sizesAdapter);

                ArrayAdapter<String> SizesAdapter2 = new ArrayAdapter(promptsView.getContext(), android.R.layout.simple_spinner_item, sizes);
                SizesAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                PantsSizesDropDown.setText(items.get(position).getBottomSize());
                PantsSizesDropDown.setAdapter(SizesAdapter2);

                ArrayAdapter<String> shoesSizesAdapter = new ArrayAdapter(promptsView.getContext(), android.R.layout.simple_spinner_item, shoe_sizes);
                shoesSizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ShoesSizesDropDown.setText(items.get(position).getFootSize());
                ShoesSizesDropDown.setAdapter(shoesSizesAdapter);

                AdressEditText.setText(items.get(position).getAdress());

                //вывод диалогового окна
                mDialogBuilder.setView(promptsView);
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();


                //обработка нажатия на кнопку "сохранить"
                Button saveChangesSubscription = promptsView.findViewById(R.id.save_changes_subscription_btn);
                saveChangesSubscription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference subscriptionRef = db.collection("UsersData")
                                .document(userId)
                                .collection("Subscribes")
                                .document(items.get(position).getId());
                        subscriptionRef
                                .update("TopSize", topSizesDropDown.getText().toString(),
                                        "PantsSIze", PantsSizesDropDown.getText().toString(),
                                        "FootSize", ShoesSizesDropDown.getText().toString(),
                                        "Adress", AdressEditText.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                                        TopSizeTV.setText("Размер верха: " + topSizesDropDown.getText().toString());
                                        PantsSizeTV.setText("Размер низа: " + PantsSizesDropDown.getText().toString());
                                        ShoesSizeTV.setText("Размер обуви: " + ShoesSizesDropDown.getText().toString());
                                        AdressTv.setText("Адрес: " + items.get(position).getAdress().toString());
                                        alertDialog.hide();


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error updating document", e);
                                    }
                                });
                    }
                });


            }
        });


    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}
