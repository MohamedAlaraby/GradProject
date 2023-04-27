@file:Suppress("DEPRECATION")
package com.example.android.gradproject.ui.home
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.gradproject.ConnectionLiveData
import com.example.android.gradproject.MealsAdapter
import com.example.android.gradproject.MealsViewModel
import com.example.android.gradproject.ui.loginscreen.LoginActivity
import com.example.android.gradproject.R
import com.example.android.gradproject.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var adapter: MealsAdapter
    private lateinit var mAuth: FirebaseAuth
    private var _binding: FragmentHomeBinding? = null
    lateinit var gsio: GoogleSignInOptions
    lateinit var gsic: GoogleSignInClient
    private val viewModel:MealsViewModel by viewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mAuth=FirebaseAuth.getInstance()
         adapter=MealsAdapter()
        setHasOptionsMenu(true)
        viewModel.getMeals()


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gsio = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        gsic = GoogleSignIn.getClient(requireActivity(), gsio)
        //display the categories.
        lifecycleScope.launch {
            viewModel.categories.collect{
                 adapter.submitList(it?.categories)
                 binding.categoryRv.adapter=adapter
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signout -> {
                signedOut()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun signedOut(){
        if (mAuth.currentUser !=null){
          mAuth.signOut()
          gsic.signOut()
        }
        if (mAuth.currentUser==null  || gsic.signOut().isComplete){
            startActivity(Intent(this@HomeFragment.activity, LoginActivity::class.java))
            this@HomeFragment.activity?.finish()
        }
    }
}