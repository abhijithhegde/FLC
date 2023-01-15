package com.example.firebase_auth;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.Layout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptor extends FirebaseRecyclerAdapter<Users, Adaptor.viewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adaptor(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    FirebaseStorage storage;
    FirebaseDatabase database;

    public static class Holder extends RecyclerView.ViewHolder {
        TextView name1;
        TextView usn1;
        ImageView img2;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name1 = itemView.findViewById(R.id.viewName);
            usn1 = itemView.findViewById(R.id.viewUSN);
            img2 = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Users model) {
        holder.name.setText(model.getName());
        holder.usn.setText(model.getUsn());

        Glide.with(holder.img1.getContext())
                .load(model.getImageURI())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img1);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //storage = FirebaseStorage.getInstance();
        //database = FirebaseDatabase.getInstance();


        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext(), R.style.viewprofile);

                dialog.setContentView(R.layout.viewprofile);

                TextView nameView = dialog.findViewById(R.id.viewName);
                TextView usnView = dialog.findViewById(R.id.viewUSN);
                TextView mailView = dialog.findViewById(R.id.viewEmail);
                ImageView imageView = dialog.findViewById(R.id.imageView);
                Picasso.get().load(model.getImageURI()).into(imageView);
                nameView.setText(model.getName());
                usnView.setText(model.getUsn());
                mailView.setText(model.getEmail());

                dialog.show();
            }
        });
        if (currentUser != null) {
            String userId = currentUser.getUid();
            if (userId.equals("iKFCWCRN2hcKCeXz5pTtJJV6idv2")) {
                holder.name.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                        builder.setTitle("Confirm Delete?");
                        builder.setMessage("This action is irreversible");

                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase.getInstance().getReference().child("user").child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(holder.name.getContext(), "Canceled", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                        return false;
                    }
                });

            }
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_item, parent, false);
        return new viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder {
        CircleImageView img1;
        ImageView img2;
        TextView name, usn, name1, usn1;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img1 = (CircleImageView) itemView.findViewById(R.id.img);
            img2 = (ImageView) itemView.findViewById(R.id.imageView);
            name = (TextView) itemView.findViewById(R.id.displayName);
            name1 = (TextView) itemView.findViewById(R.id.viewName);
            usn = (TextView) itemView.findViewById(R.id.displayUsn);
            usn1 = (TextView) itemView.findViewById(R.id.viewUSN);
        }
    }
}
