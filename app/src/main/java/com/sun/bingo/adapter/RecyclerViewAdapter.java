package com.sun.bingo.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.framework.dialog.ToastTip;
import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.control.manager.ImageManager;
import com.sun.bingo.model.BingoEntity;
import com.sun.bingo.model.UserEntity;
import com.sun.bingo.ui.activity.MainActivity;
import com.sun.bingo.ui.activity.UserInfoActivity;
import com.sun.bingo.util.DateUtil;
import com.sun.bingo.util.NetWorkUtil;
import com.sun.bingo.util.ShareUtil;
import com.sun.bingo.widget.GroupImageView.GroupImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BingoEntity> mEntities;
    private UserEntity userEntity;
    private int type = HANDLE_NORMAL;

    public static final int HANDLE_NORMAL = 0;
    public static final int HANDLE_CANCEL_FAVORITE = 1;

    private static final int TYPE_LIST = 0;
    private static final int TYPE_FOOT_VIEW = 1;
    private ImageManager mImageManager;

    private View footView;

    public RecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public RecyclerViewAdapter(Context context, List<BingoEntity> entities) {
        this(context);
        this.mEntities = entities;
        userEntity = BmobUser.getCurrentUser(context, UserEntity.class);
        mImageManager = new ImageManager(context);
    }

    public void setHandleType(int type) {
        this.type = type;
    }

    @Override
    public int getItemCount() {
        return mEntities.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOT_VIEW;
        } else {
            return TYPE_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case TYPE_LIST:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_main, parent, false);
                viewHolder = new ListViewHolder(view);
                break;
            case TYPE_FOOT_VIEW:
                footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_footview_layout, parent, false);
                footView.setVisibility(View.GONE);
                viewHolder = new FootViewHolder(footView);
                break;
            default:
                viewHolder = new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_main, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            final ListViewHolder listViewHolder = (ListViewHolder) holder;
            final BingoEntity entity = mEntities.get(position);
            final UserEntity userEntity = entity.getUserEntity();
            final int mPosition = position;

            if (userEntity != null && !TextUtils.isEmpty(userEntity.getUserAvatar())) {
                mImageManager.loadCircleImage(userEntity.getUserAvatar(), listViewHolder.ivUserAvatar);

            } else {
                mImageManager.loadCircleResImage(R.drawable.ic_user, listViewHolder.ivUserAvatar);
            }

            if (userEntity != null && !TextUtils.isEmpty(userEntity.getNickName())) {
                listViewHolder.tvNickName.setText(userEntity.getNickName());
            } else {
                listViewHolder.tvNickName.setText("NULL");
            }

            listViewHolder.tvDescribe.setText(entity.getDescribe());
            listViewHolder.tvprice.setText("价格"+entity.getPrice()+"元");
            if (entity.getCreateTime() > 0) {
                listViewHolder.tvTime.setVisibility(View.VISIBLE);
                listViewHolder.tvTime.setText(DateUtil.getDateStr(mContext, entity.getCreateTime()));
            } else {
                listViewHolder.tvTime.setVisibility(View.GONE);
            }

            if (entity.getImageList() != null && entity.getImageList().size() > 0) {
                listViewHolder.givImageGroup.setVisibility(View.VISIBLE);
                listViewHolder.givImageGroup.setPics(entity.getImageList());
            } else {
                listViewHolder.givImageGroup.setVisibility(View.GONE);
            }

            listViewHolder.rlRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetWorkUtil.isLinkAvailable(entity.getWebsite())) {
                        NavigateManager.gotoBingoDetailActivity(mContext, entity);
                    }
                }
            });

            listViewHolder.ivItemMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopMenu(v, mPosition);
                }
            });

            listViewHolder.ivUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.getUserEntity() != null) {
                        NavigateManager.gotoUserInfoActivity(mContext, entity.getUserEntity());
                    }
                }
            });
        }
    }

    public void setLoadMoreViewText(String text) {
        if (footView == null) return;
        ((TextView) ButterKnife.findById(footView, R.id.tv_loading_more)).setText(text);
        notifyItemChanged(getItemCount());
    }

    public void setLoadMoreViewVisibility(int visibility) {
        if (footView == null) return;
        footView.setVisibility(visibility);
        notifyItemChanged(getItemCount());
    }

    public boolean isLoadMoreShown() {
        if (footView == null) return false;
        return footView.isShown();
    }

    public String getLoadMoreViewText() {
        if (footView == null) return "";
        return ((TextView) ButterKnife.findById(footView, R.id.tv_loading_more)).getText().toString();
    }

    /**
     * 弹出菜单
     */
    private void showPopMenu(View ancho, final int position) {
        userEntity = BmobUser.getCurrentUser(mContext, UserEntity.class);
        final BingoEntity entity = mEntities.get(position);
        List<String> favoriteList = userEntity.getFavoriteList();

        PopupMenu popupMenu = new PopupMenu(mContext, ancho);
        popupMenu.getMenuInflater().inflate(R.menu.item_pop_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pop_favorite:
                        if (item.getTitle().equals("取消收藏")) {
                            cancelFavoriteBingo(entity.getObjectId(), position);
                        } else {
                            handleFavoriteBingo(entity.getObjectId());
                        }
                        userEntity = BmobUser.getCurrentUser(mContext, UserEntity.class);
                        return true;
                    case R.id.pop_share_sina:
                        if (mContext instanceof MainActivity) {
                            MainActivity mainActivity = (MainActivity) mContext;
                            mainActivity.sendMultiMessageToSina(entity);
                        } else if (mContext instanceof UserInfoActivity) {
                            UserInfoActivity userInfoActivity = (UserInfoActivity) mContext;
                            userInfoActivity.sendMultiMessageToSina(entity);
                        }
                        break;
                    case R.id.pop_share:
                        ShareUtil.share(mContext, entity.getDescribe() + entity.getWebsite() + "\n[来自" + mContext.getString(R.string.app_name) + "的分享]");
                        return true;
                    case R.id.pop_delete:
                        new MaterialDialog.Builder(mContext)
                                .content("确认删除该鱼苗么？")
                                .contentColor(mContext.getResources().getColor(R.color.font_black_3))
                                .positiveText(R.string.ok)
                                .negativeText(R.string.cancel)
                                .negativeColor(mContext.getResources().getColor(R.color.font_black_3))
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        deleteMyBingo(position);
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                    }
                                })
                                .show();
                        break;
                }
                return false;
            }
        });
        if (type == HANDLE_CANCEL_FAVORITE || (favoriteList != null && favoriteList.indexOf(entity.getObjectId()) >= 0)) {
            MenuItem menuItem = popupMenu.getMenu().findItem(R.id.pop_favorite);
            menuItem.setTitle("取消收藏");
        }
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.pop_delete);
        if (entity.getUserId().equals(userEntity.getObjectId())) {
            menuItem.setVisible(true);
        } else {
            menuItem.setVisible(false);
        }
        popupMenu.show();
    }

    /**
     * 收藏
     */
    private void handleFavoriteBingo(String bingoId) {
        List<String> favoriteList = userEntity.getFavoriteList();
        if (favoriteList == null) {
            favoriteList = new ArrayList<>();
        }
        if (favoriteList.indexOf(bingoId) >= 0) {
            ToastTip.show("您已收藏过了");
            return;
        }
        favoriteList.add(bingoId);
        userEntity.setFavoriteList(favoriteList);
        userEntity.update(mContext, userEntity.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastTip.show("收藏成功");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastTip.show("收藏失败");
            }
        });
    }

    /**
     * 取消收藏
     */
    private void cancelFavoriteBingo(String bingoId, final int position) {
        List<String> favoriteList = userEntity.getFavoriteList();
        if (favoriteList == null) {
            favoriteList = new ArrayList<>();
        }
        if (favoriteList.indexOf(bingoId) < 0) {
            ToastTip.show("您已取消收藏了");
            return;
        }
        favoriteList.remove(bingoId);
        userEntity.setFavoriteList(favoriteList);
        userEntity.update(mContext, userEntity.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastTip.show("取消成功");
                if (type == HANDLE_CANCEL_FAVORITE) {
                    mEntities.remove(position);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int i, String s) {
                ToastTip.show("取消失败");
            }
        });
    }

    /**
     * 删除我的Bingo
     */
    private void deleteMyBingo(final int position) {
        BingoEntity entity = mEntities.get(position);
        entity.delete(mContext, new DeleteListener() {
            @Override
            public void onSuccess() {
                ToastTip.show("删除成功");
                mEntities.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastTip.show("删除失败");
            }
        });
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_user_avatar)
        ImageView ivUserAvatar;
        @Bind(R.id.tv_nick_name)
        TextView tvNickName;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.rl_user_info)
        RelativeLayout rlUserInfo;
        @Bind(R.id.iv_item_more)
        ImageView ivItemMore;
        @Bind(R.id.tv_describe)
        TextView tvDescribe;
        @Bind(R.id.giv_image_group)
        GroupImageView givImageGroup;
        @Bind(R.id.rl_root_view)
        RelativeLayout rlRootView;
        @Bind(R.id.tv_price)
        TextView tvprice;

        public ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_loading_more)
        TextView tvLoadingMore;

        public FootViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
