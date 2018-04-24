package metropolitan.com.sporters.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import metropolitan.com.sporters.Domain.Message;
import metropolitan.com.sporters.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MessageHolder>
{
    private Context mContext;
    ArrayList<Message> mAllMessages;

    public ListAdapter(Context mContext, ArrayList<Message> mAllMessages)
    {
        this.mContext = mContext;
        this.mAllMessages = mAllMessages;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.message, parent, false);

        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position)
    {
        holder.messageUser.setText(mAllMessages.get(position).getUser() + ":");
        holder.messageBody.setText(mAllMessages.get(position).getBody());
    }

    @Override
    public int getItemCount()
    {
        return mAllMessages.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder
    {
        TextView messageUser;
        TextView messageBody;

        public MessageHolder(View itemView)
        {
            super(itemView);
            messageUser = itemView.findViewById(R.id.txt_msgUser);
            messageBody = itemView.findViewById(R.id.txt_msgBody);
        }
    }
}
