package com.trebit.reststudy.ui.main.fragment

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.orhanobut.logger.Logger
import com.trebit.reststudy.*
import com.trebit.reststudy.adapter.ContentsAdapter
import com.trebit.reststudy.data.model.ContentItem
import com.trebit.reststudy.databinding.MainFragmentFirstTabBinding
import com.trebit.reststudy.ui.BaseFragment
import com.trebit.reststudy.ui.main.activity.MainActivity
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import com.trebit.reststudy.utils.RecyclerItemDecoration
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_fragment_first_tab.*
import javax.inject.Inject


/**
 * Jstargram
 * Class: ContentsFragments
 * Created by mac on 05/04/2019.
 *
 * Description:
 */

enum class DataType {
    ALL,
    SINGLE,
    MINE
}

enum class ViewType {
    GRID,
    VERTICAL
}

class ContentsFragments: BaseFragment(), ContentsAdapter.ContentEventListener {

    private var mViewType : ViewType?  = null
    private var mReqEmail : String?    = null

    private lateinit var mDataType : DataType

    @Inject
    internal lateinit var viewModelFactory : ViewModelProvider.Factory

    private lateinit var mBinding          : MainFragmentFirstTabBinding
    private lateinit var mMainViewModel    : MainViewModel
    private lateinit var mContentsAdapter  : ContentsAdapter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mViewType = it.getSerializable(KEY_VIEW_TYPE) as ViewType   // Grid, Vertical
            mReqEmail = it.getString(KEY_EMAIL)

            mDataType = mReqEmail?.let {
                // My Data
                if (it == mPref.getPrefEmail(PREF_EMAIL))
                    DataType.MINE
                // Query Data
                else DataType.SINGLE
            } ?: DataType.ALL   // All Data
        }
    }

    override fun onCreateView(inflater          : LayoutInflater,
                              container         : ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        mBinding       = DataBindingUtil.inflate(inflater, R.layout.main_fragment_first_tab, container, false)
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mBinding.viewModel = mMainViewModel
        mBinding.activity  = activity as MainActivity

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewAndAdapter(mViewType!!)
        callContentsDataAPI()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        when (mDataType) {
            DataType.ALL -> {
                mMainViewModel.allContents.observe(this, Observer {
                    it?.let { mContentsAdapter.setContentItem(it) }
                })
            }
            DataType.SINGLE -> {
            }
            DataType.MINE -> {
                mMainViewModel.myContents.observe(this, Observer {
                    it?.let {
                        if (mViewType == ViewType.GRID) {
                            mContentsAdapter.setContentItem(it.reversed())
                        } else {
                            mContentsAdapter.setContentItem(it)
                        }
                    }
                })
            }
        }
    }


    // Initializing RecyclerView and Adapter.
    private fun initViewAndAdapter(viewType: ViewType){
        when (viewType) {
            // Grid Type.
            ViewType.GRID -> {
                mContentsAdapter = ContentsAdapter(VIEW_TYPE_GRID)
                val gm = GridLayoutManager(activity, 3)
                with(rv_contentList){
                    setHasFixedSize(true)
                    layoutManager = gm
                    addItemDecoration(RecyclerItemDecoration(activity!!))
                    adapter = mContentsAdapter
                }
            }

            // Vertical Type.
            ViewType.VERTICAL -> {
                mContentsAdapter = ContentsAdapter(VIEW_TYPE_VERTICAL).apply {
                    mContentEventListener = this@ContentsFragments
                }
                val lm = LinearLayoutManager(activity).apply {
                    reverseLayout = true
                    stackFromEnd  = true
                }

                with (rv_contentList){
                    setHasFixedSize(true)
                    layoutManager = lm
                    adapter = mContentsAdapter
                }
            }
        }
    }

    // Call APIs about Contents.
    private fun callContentsDataAPI() {
        when (mDataType) {
            DataType.ALL -> mMainViewModel.getContents("all")

            DataType.SINGLE -> {}

            DataType.MINE -> mMainViewModel.getContents("mine", mReqEmail)
        }
    }


    // Clicked More
    override fun clickedMore(item: ContentItem, pos: Int) {
        val view = layoutInflater.inflate(R.layout.dialog_content_more, null)
        val tvRmContent = view?.findViewById<TextView>(R.id.tv_removeContent)

        val builder  = AlertDialog.Builder(activity)
            .setView(view)
            .create()
        builder.show()

        tvRmContent?.setOnClickListener {
            mMainViewModel.removeContent(item.contents_id, mPref.getPrefEmail(PREF_EMAIL))
            builder.dismiss()
        }
    }

    // Clicked Writer
    override fun clickedWriter(email: String) {
        Logger.d(email)
        mBinding.activity?.addUserFragment(UserHomeFragment.newInstance(email, "query"))
    }

    companion object {
        @JvmStatic
        fun newInstance(viewType: ViewType, email: String? = null) =
            ContentsFragments().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_VIEW_TYPE, viewType)
                    putString(KEY_EMAIL, email)
                }
            }
    }
}












//    private fun initRecyclerView() {
//        // swipe refresh setting.
//        srl_swipeLayout.setOnRefreshListener {
//            mMainViewModel.getContents()
//            srl_swipeLayout.isRefreshing = false
//        }

//        // recyclerview click event process.
//        ItemClickSupport.addTo(rv_contentList)
//            .setOnItemClickListener(object: ItemClickSupport.OnItemClickListener {
//                // Single Clicked.
//                override fun onItemClicked(recyclerView: RecyclerView?, position: Int, v: View?) {
//                    Logger.d("clicked : $position")
//                }
//
//                // Double Clicked.
//                override fun onItemDoubleClicked(recyclerView: RecyclerView?, position: Int, v: View?) {
//                    Logger.d("double Clicked")
//                    val tv = v?.findViewById<TextView>(R.id.tv_favoriteInfo)
//                    tv?.visibility = if (tv?.visibility == View.VISIBLE) View.GONE else View.VISIBLE
//                }
//            })
//
//        // call get All Contents API.
//        mMainViewModel.getContents()
//    }