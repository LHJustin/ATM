package com.tom.atm

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.tom.atm.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    var remember = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireContext().getSharedPreferences("atm", Context.MODE_PRIVATE)
        val checked = pref.getBoolean("rem_username", false)
        binding.cbRemember.isChecked = checked
        binding.cbRemember.setOnCheckedChangeListener {compoundButton, checked ->
            remember = checked
            pref.edit().putBoolean("rem_username", remember).apply()
            if (!checked){
                pref.edit().putString("USER", "").apply()
            }
        }
        val prefUser = pref.getString("USER", "")
        if (prefUser != "") {
            binding.edUsername.setText(prefUser)
        }

        binding.buttonFirst.setOnClickListener {
            //Login stuff
            val username = binding.edUsername.text.toString()
            val password = binding.edPassword.text.toString()
            if (username == "jack" && password == "1234") {
//                save username to preferences
//                val pref = requireContext().getSharedPreferences("atm", Context.MODE_PRIVATE)
                if (remember)
                    pref.edit()
                    .putString("USER", username)
                    .putInt("LEVEL", 3)
                    .apply() //.commit()
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }else {
                //error
                AlertDialog.Builder(requireContext())
                    .setTitle("Login problem")
                    .setMessage("Login Failed")
                    .setPositiveButton("OK",null)
                    .show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}