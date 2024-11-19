package vn.edu.hust.studentman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(
  private val students: MutableList<StudentModel>,
  private val activity: AppCompatActivity
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(
      R.layout.layout_student_item, parent, false
    )
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId

    // Khi nhấn nút Edit
    holder.imageEdit.setOnClickListener {
      showEditDialog(student, position)
    }

    // khi nhấn nút Xóa
    holder.imageRemove.setOnClickListener{
      showDeleteDialog(student,position)
    }
  }

  private fun showEditDialog(student: StudentModel, position: Int) {
    val dialogView = LayoutInflater.from(activity).inflate(R.layout.layout_alert_dialog, null)
    val editHoten = dialogView.findViewById<EditText>(R.id.edit_hoten)
    val editMssv = dialogView.findViewById<EditText>(R.id.edit_mssv)

    editHoten.setText(student.studentName)
    editMssv.setText(student.studentId)

    AlertDialog.Builder(activity)
      .setTitle("Chỉnh sửa thông tin")
      .setView(dialogView)
      .setPositiveButton("OK") { _, _ ->
        val updatedName = editHoten.text.toString()
        val updatedId = editMssv.text.toString()
        if (updatedName.isNotBlank() && updatedId.isNotBlank()) {
          students[position] = StudentModel(updatedName, updatedId)
          notifyItemChanged(position)
        }
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  private fun showDeleteDialog(student: StudentModel, position: Int) {
    AlertDialog.Builder(activity)
      .setTitle("Xác nhận xóa")
      .setMessage("Bạn có chắc chắn muốn xóa sinh viên ${student.studentName} không?")
      .setPositiveButton("Xóa") { _, _ ->
        // Lưu thông tin sinh viên vừa bị xóa
        val deletedStudent = student
        val deletedPosition = position

        // Xóa sinh viên khỏi danh sách
        students.removeAt(position)
        notifyItemRemoved(position)

        // Hiển thị Snackbar với tùy chọn Undo
        Snackbar.make(
          activity.findViewById(android.R.id.content), // View gốc
          "Đã xóa ${student.studentName}", // Thông báo
          Snackbar.LENGTH_LONG // Thời gian hiển thị
        ).setAction("Undo") {
          // Khôi phục sinh viên
          students.add(deletedPosition, deletedStudent)
          notifyItemInserted(deletedPosition)
        }.show()
      }
      .setNegativeButton("Hủy", null)
      .show()
  }
}
