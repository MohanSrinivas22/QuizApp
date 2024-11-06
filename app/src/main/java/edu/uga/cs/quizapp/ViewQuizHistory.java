package edu.uga.cs.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter class for the RecyclerView used to display quiz history.
 */
public class ViewQuizHistory extends RecyclerView.Adapter<ViewQuizHistory.ViewQuizHistoryHolder> implements Filterable {

    private final Context context;

    private List<QuizResultPOJO> values;
    private List<QuizResultPOJO> originalValues;

    /**
     * Constructs a ViewQuizHistory adapter with the given context and quiz results list.
     *
     * @param context     The context.
     * @param quizResults The list of quiz results.
     */
    public ViewQuizHistory(Context context, List<QuizResultPOJO> quizResults) {
        this.context = context;
        this.values = quizResults;
        this.originalValues = new ArrayList<QuizResultPOJO>(quizResults);
    }

    /**
     * Syncs the original values with the current values.
     */
    public void sync() {
        originalValues = new ArrayList<QuizResultPOJO>(values);
    }

    /**
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     *
     * <p>This method is usually implemented by {@link Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        return null;
    }


    /**
     * ViewHolder class for the ViewQuizHistory adapter.
     */
    public static class ViewQuizHistoryHolder extends RecyclerView.ViewHolder {

        TextView quizId;
        TextView quizResult;
        TextView quizDate;

        /**
         * Constructs a ViewQuizHistoryHolder with the given View.
         *
         * @param itemView The View.
         */
        public ViewQuizHistoryHolder(@NonNull View itemView) {
            super(itemView);
            quizId = itemView.findViewById(R.id.quizId);
            quizResult = itemView.findViewById(R.id.quizResult);
            quizDate = itemView.findViewById(R.id.quizDate);
        }
    }

    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewQuizHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz, parent, false);
        return new ViewQuizHistoryHolder(view);
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewQuizHistoryHolder holder, int position) {
        QuizResultPOJO quiz = values.get(position);
        holder.quizId.setText(String.valueOf(quiz.getId()));
        holder.quizResult.setText(String.valueOf(quiz.getResult()));
        holder.quizDate.setText(quiz.getDate());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (values != null) {
            return values.size();
        } else {
            return 0;
        }
    }
}